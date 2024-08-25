import { Component, OnInit, Inject, AfterViewInit } from '@angular/core';
import { Location, DOCUMENT } from '@angular/common';

import { Animal } from '../animal';
import { AnimalService } from '../animal.service';
import { LoginService } from '../login.service';

@Component({
  // standalone: true,
  selector: 'app-animals',
  templateUrl: './animals.component.html',
  styleUrls: ['./animals.component.css']
})
export class AnimalsComponent implements OnInit, AfterViewInit {
  animals: Animal[] = [];
  types: string[] = [];
  private document: Document;

  constructor(private animalService: AnimalService, private loginService: LoginService, @Inject(DOCUMENT) document: Document) {
    this.document = document;
  }

  ngOnInit(): void {
    this.getAnimals();
  }

  ngAfterViewInit(): void {
    

    const curLogin = this.loginService.getCurrentLogin();
    if ((curLogin?.level || -1) <= 0) {
      const div = this.document.getElementById("creation-div");

      if (div instanceof HTMLElement) {
        div.hidden = true;
      }
    }
  }

  private getSearchBoxValue() {
    let searchBox: any = this.document.getElementById("search-animal");
    const searchText = searchBox?.value || "";
    return searchText
  }

  search(isAny=false): void {
    if (isAny) {
      const searchText = this.getSearchBoxValue();
      this.animalService.searchAnimal(searchText).subscribe(arr => this.animals = arr);
      return;
    }

    let ele = document.querySelector('input[name="type_search"]:checked') as HTMLInputElement;
    this.searchType(ele?.value || "any");
  }

  searchType(type: string): void {
    if (type == "any") {
      this.search(true);
      return;
    }
    this.animalService.searchType(type).subscribe(arr => this.animals = arr.filter(animal => !animal.sold).filter(animal => animal.name.toLowerCase().includes(this.getSearchBoxValue().toLowerCase())));
  }
  
  getAnimals(): void {
    this.animalService.getAnimals().subscribe(animals => {
      this.animals = animals.filter(animal => !animal.sold);

      this.types = ["any"];
      this.animals.forEach(a => {
        if (!this.types.includes(a.type)) {
          this.types.push(a.type);
        }
      });
    });
  }
  
  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.animalService.createAnimal({ name } as Animal)
      .subscribe(animal => {
        this.animals.push(animal);
      });
  }

}