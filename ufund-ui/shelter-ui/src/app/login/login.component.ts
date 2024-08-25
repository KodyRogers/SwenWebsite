import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location, DOCUMENT } from '@angular/common';

import { MessageService } from '../message.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  document: Document;
  isSignedIn: Boolean = false;

  constructor (private route: ActivatedRoute, private location: Location, 
              @Inject(DOCUMENT) document: Document, private loginService: LoginService, 
              private messageService: MessageService) {
    this.document = document;
  }

  ngAfterViewInit(): void {
    console.log('login component');
    this.isSignedIn = this.loginService.isLoggedIn();

    let nav = this.document.getElementById("navigator");
    if (nav == null) return;
    nav.hidden = !this.isSignedIn;
  }

  setOutput(boxid: string, text: string, isError: boolean) {
    let outputBox: any = this.document.getElementById(boxid);

    outputBox.innerHTML = text;
    outputBox.style.color = !isError ? "green" : "red";
  }

  signup(loginBoxID: string, passwordBoxID: string, outputBoxID: string) {
    console.log("sign up button");

    let loginBox: any = this.document.getElementById(loginBoxID);
    let passwordBox: any = this.document.getElementById(passwordBoxID);

    const testUser = loginBox?.value || "";
    const testPassword = passwordBox?.value || "";

    if (testUser.length < 3) {
      this.setOutput(outputBoxID, "User name must be 3 or more characters.", true);
      return;
    }
    else if (testPassword.length < 3) {
      this.setOutput(outputBoxID, "Password name must be 3 or more characters.", true);
      return;
    }
    else if (testUser.length > 16) {
      this.setOutput(outputBoxID, "User name must be fewer than 16 characters.", true);
      return;
    }
    else if (testPassword.length > 32) {
      this.setOutput(outputBoxID, "Password must be fewer than 32 characters.", true);
      return;
    }

    this.loginService.attemptSignup(testUser, testPassword).subscribe(userObj => {
      console.log(userObj);
      if (userObj == null) {
        this.setOutput(outputBoxID, "This user name is already taken.", true)
      }
      else if (testUser == userObj?.name && testPassword == userObj?.password) {
        this.setOutput(outputBoxID, "Succsfully signed up! New account created.", false)
      } else {
        this.setOutput(outputBoxID, "Error, something went wrong, try again later.", true)
      }
    });
  }

  login(loginBoxID: string, passwordBoxID: string, outputBoxID: string): void {
    let loginBox: any = this.document.getElementById(loginBoxID);
    let passwordBox: any = this.document.getElementById(passwordBoxID);

    const testUser = loginBox?.value;
    const testPassword = passwordBox?.value;

    this.loginService.attemptLogin(testUser, testPassword).subscribe(userObj => {
      console.log(userObj);

      if (testUser == userObj.name && testPassword == userObj.password) {
        this.setOutput(outputBoxID, "Succsfully logged in!", false);
        this.ngAfterViewInit();
      } else {
        this.setOutput(outputBoxID, "Error, login does not exist.", true);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
  logOut(): void {
    this.loginService.logOut();
    window.location.replace("http://localhost:4200/login");
  }
}
