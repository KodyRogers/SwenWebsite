import { Injectable, Inject } from '@angular/core';

import { BehaviorSubject, Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { MessageService } from './message.service';
import { User } from './user';
import { DOCUMENT } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  private dataKey = "currentUserLogin";
  private loginURL: string = 'http://localhost:8080/users/';  // URL to web api
  private defaultUser: User = {
    "name": "placeholder",
    "level": 0,
    "basket": [],
    "password": ""
  };
  httpOptions = {headers: new HttpHeaders({ 'Content-Type': 'application/json' })};

  constructor(private http: HttpClient, 
    private messageService: MessageService,
    @Inject(DOCUMENT) document: Document) {
      this.currentUserSubject = new BehaviorSubject<User>(this.defaultUser); // Initially, no user
      this.currentUser = this.currentUserSubject.asObservable();
    }

  refreshUser() {
    console.log("login service init");
    if (this.isLoggedIn()) {
      console.log("login service refresh");
      let user = this.getCurrentLogin();
      this.attemptLogin(user.name, user.password).subscribe(() => {
        window.location.reload();
      });
    }
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error); // log to console instead

      this.messageService.add(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  attemptLogin(userName: string, password: string) {
    const user = this.http.get<User>(this.loginURL + userName).pipe(
      tap(_ => this.messageService.add('fetched users')),
      catchError(this.handleError<User>('attemptLogin', this.defaultUser))
    );
    this.messageService.add('UserService: fetched user');

    user.subscribe(uObj => {
      if (uObj.name == this.defaultUser.name || uObj.password == this.defaultUser.password || uObj.password != password) {
        return;
      }
      console.log("set login");
      localStorage.setItem("currentUserLogin", JSON.stringify(uObj));
      this.currentUserSubject.next(uObj);
    });

    return user;
  }

  attemptSignup(userName: string, password: string) {

    const attemptUser = {
      "name": userName,
      "level": 0,
      "basket": [],
      "password": password
    }

    const user = this.http.post<User>(this.loginURL, attemptUser, this.httpOptions).pipe(
      tap((newUser: User) => this.messageService.add(`sign up name=${newUser.name}`)),
      catchError(this.handleError<User>('attemptSignup'))
    );

    this.messageService.add('UserService: created user');

    return user;
  }

  private updatedBasket(user: User) {
    console.log(`update user with basket: [${user.basket}]`);

    return this.http.put<User>("http://localhost:8080/users", user, this.httpOptions).pipe(
      tap((newUser: User) => {
        console.log("it worked?");
      }),
      catchError(this.handleError<User>('updatedBasket'))
    ).subscribe(() => {});
  }

  basketAdd(ele: number) {
    const obj = this.getCurrentLogin();
    if (obj.basket.indexOf(ele) > -1) {
      return;
    }
    obj.basket.push(ele);
    localStorage.setItem(this.dataKey, JSON.stringify(obj));
    this.updatedBasket(obj);
  }

  basketRemove(ele: number) {
    const obj = this.getCurrentLogin();
    let removeAtindex = obj.basket.indexOf(ele)
    if (removeAtindex !== -1) {
      obj.basket.splice(removeAtindex, 1)
      localStorage.setItem(this.dataKey, JSON.stringify(obj));
    }
    this.updatedBasket(obj);
  }

  getCurrentLogin() {
    const obj = JSON.parse(localStorage.getItem("currentUserLogin") || "") as User;
    this.currentUserSubject.next(obj);
    return obj;
  }

  isLoggedIn() {
    return (JSON.parse(localStorage.getItem("currentUserLogin") || "") as User).password != "";
  }

  logOut() {
    localStorage.setItem("currentUserLogin", JSON.stringify(this.defaultUser));
    this.currentUserSubject.next(this.defaultUser);
  }

  updateAnimal(user: User): Observable<any> {
    return this.http.put(this.loginURL, user, this.httpOptions).pipe(
      tap(_ => this.messageService.add('updated username=${user.name}')),
      catchError(this.handleError<any>('updateUser'))
    );
  }

  userSub() {
    return this.currentUser;
  }


  checkout() {
    let user = this.getCurrentLogin();

    console.log(`checkout: [${user.basket}]`);
    return this.http.put<Array<number>>("http://localhost:8080/users/checkout", user, this.httpOptions).pipe(
      tap((newUser: Array<number>) => {
        // empty on purpose
      }),
    );
  }
}