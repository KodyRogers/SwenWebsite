import { Component, OnInit } from '@angular/core';
import { Animal } from '../animal';
import { AnimalService } from '../animal.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  animals: Animal[] = [];
  goalTotal: number = 10000;
  goalReached: number = 0;
  goalPercent: number = 0;

  constructor(private animalService: AnimalService) { }

  ngOnInit(): void {
    this.getAnimals();
  }

  
  
  getAnimals(): void {
    let filterAnimals: Animal[] = [];
    this.animalService.getAnimals()
      .subscribe(animals => {

        for (let i = animals.length-1; i >= 0; i--) {
          if (animals[i].sold == false && filterAnimals.length < 4) {
            filterAnimals.push(animals[i]);
          }
        }

        this.animals = filterAnimals;

        this.goalReached = 0;
        this.goalPercent = 0;

        animals.forEach(a => {
          if (a.sold) this.goalReached += a.cost;
        });

        this.goalPercent = this.goalReached / this.goalTotal;
      });
  }
}