import {Component, Input} from '@angular/core';
import {Animal} from '../animal';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { AnimalService } from '../animal.service';
import { LoginService } from '../login.service';

@Component({
  // standalone: true,
  selector: 'app-animal-detail',
  templateUrl: './animal-detail.component.html',
  styleUrls: ['./animal-detail.component.css'],
  // imports: [FormsModule, NgIf, UpperCasePipe],
})
export class AnimalDetailComponent {
  @Input() animal?: Animal;

  constructor(
    private route: ActivatedRoute,
    private animalService: AnimalService,
    private location: Location,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.getAnimal();

    // set all the routs based on our login level
    // routerLink="/detail/{{animal.id}}"
    // routerLink="/modify/{{animal.id}}"
    const curLogin = this.loginService.getCurrentLogin();
    // if perms too high change page to modify
    if ((curLogin?.level || -1) > 0) {
      window.location.replace(window.location.href.replace("detail", "modify"));
    }
  }
  
  getAnimal(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.animalService.getAnimal(id)
      .subscribe(animal => this.animal = animal);
  }

  goBack(): void {
    this.location.back();
  }

  addCart(): void {
      if (this.animal) {
        this.loginService.basketAdd(this.animal.id)
      }
  }

}
