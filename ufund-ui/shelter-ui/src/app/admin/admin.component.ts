import { Component, OnInit, Inject, AfterViewInit } from '@angular/core';
import { Location, DOCUMENT } from '@angular/common';

import { Animal } from '../animal';
import { User } from '../user';
import { AnimalService } from '../animal.service';

import { Transaction } from '../transaction';
import { TransactionService } from '../transaction.service';
import { LoginService } from '../login.service';
import { UserService } from '../user.service';

@Component({
  // standalone: true,
  selector: 'app-animals',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit, AfterViewInit {
  transactions: Transaction[] = [];
  animalsMap: Map<number, Animal> = new Map();
  private document: Document;
  users: Array<User> = [];

  constructor(
    private transactionService: TransactionService,
    private animalService: AnimalService,
    private loginService: LoginService,
    private userService: UserService,
    @Inject(DOCUMENT) document: Document
    ) {
    this.document = document;
  }

  ngOnInit(): void {
    this.getAnimals();
    this.getUsers();
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
  
  getAnimals(): void {
    this.animalService.getAnimals().subscribe(animals => {
      animals.forEach(a => {
        this.animalsMap.set(a.id, a);
      });
    });

    this.transactionService.getTransactions().subscribe(
      trans => {
        this.transactions = trans;
      }
    );
  }
  
  count: number = 0;

  getUsers(): void {
    this.userService.getUsers()
    .subscribe(users => {
      this.users = users;
      this.count++;
    });
  }

  millisecondsToDate(time: number): String {
    const d = new Date(time);
    return d.toLocaleString();
  }
}