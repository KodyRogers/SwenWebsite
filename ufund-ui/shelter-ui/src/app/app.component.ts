import { Component, AfterViewInit, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import {LoginService} from './login.service';
import { User } from './user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'ShelterUI';
  private document: Document;
  isSignedIn: boolean = false;
  public currentUser: User = {
    "name": "placeholder",
    "level": 0,
    "basket": [],
    "password": ""
  };

  constructor (private loginService: LoginService, @Inject(DOCUMENT) document: Document) {
    this.document = document;
    this.loginService.userSub().subscribe(user => {
      this.currentUser = user;
    })
  }

  ngAfterViewInit(): void {
    console.log("run");
    const curLogin = this.loginService.getCurrentLogin();
    console.log("hide");
    const div = this.document.getElementById("basket-route");

    this.isSignedIn = this.loginService.isLoggedIn();

    if (!this.isSignedIn && !window.location.toString().includes("login")) {
      window.location.replace("http://localhost:4200/login");
      return;
    }

    if ((curLogin?.level || -1) > 0) {
      
      if (div instanceof HTMLDivElement) {
        console.log("hidden");
        div.style.display = "none";
      }
    }
    else if (div instanceof HTMLDivElement) {
      console.log("shown");
      div.style.display = "";
    }
  }
}
