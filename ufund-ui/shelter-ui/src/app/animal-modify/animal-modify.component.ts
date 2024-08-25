import {Component, Input} from '@angular/core';
import {Animal} from '../animal';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { AnimalService } from '../animal.service';

@Component({
  // standalone: true,
  selector: 'app-animal-modify',
  templateUrl: './animal-modify.component.html',
  styleUrls: ['./animal-modify.component.css'],
  // imports: [FormsModule, NgIf, UpperCasePipe],
})
export class AnimalModifyComponent {
  animals: Animal[] = [];
  @Input() animal?: Animal;
  
  constructor(
    private route: ActivatedRoute,
    private animalService: AnimalService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getAnimal();
  }
  
  getAnimal(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.animalService.getAnimal(id)
      .subscribe(animal => this.animal = animal);
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.animal) {
      this.animalService.updateAnimal(this.animal)
        .subscribe(() => this.goBack());
    }
  }

  delete(): void {
    if (this.animal) {
      this.animalService.deleteAnimal(this.animal.id)
        .subscribe(() => this.goBack());
    }
  }

  redirect(): void {
    window.location.replace("http://localhost:4200/animals");
  }

}