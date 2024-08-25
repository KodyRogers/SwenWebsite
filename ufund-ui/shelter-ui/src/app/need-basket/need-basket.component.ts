import { Component } from '@angular/core';
import { Animal } from '../animal';
import { AnimalService } from '../animal.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-need-basket',
  templateUrl: './need-basket.component.html',
  styleUrls: ['./need-basket.component.css']
})
export class NeedBasketComponent {
  animals: Animal[] = [];
  totalCost: number = 0;

  constructor(private loginService: LoginService, private animalService: AnimalService) { }

  ngOnInit(): void {
    this.getAnimals();
  }

  checkout(): void {
    console.log("checkout pressed");
    this.loginService.checkout().subscribe(arr => {
      console.log(`checkout subscribed ${arr}`);

      this.loginService.refreshUser();
    }, err => {
      let message = "could not find 1 or more animals in basket, they may have already been checked out by another user:";

      err.error?.forEach((a: any) => {
        console.log(a);
        message += `\n${a.name}`;
      });

      alert(message);
    });
  }

  getAnimals(): void {
    const usr = this.loginService.getCurrentLogin();
    const basket = usr?.basket || [];

    this.animals = [];
    this.totalCost = 0;

    this.animalService.getAnimals().subscribe(animals => {
      animals.forEach(a => {
        if (basket.includes(a.id)) {
          this.animals.push(a);
          this.totalCost += a.cost;
        }
      });
    });
  }

  delete(animal: Animal): void {
    this.loginService.basketRemove(animal.id);
    this.getAnimals();
  }
}
