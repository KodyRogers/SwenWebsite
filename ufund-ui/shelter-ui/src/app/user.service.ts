import { Injectable } from '@angular/core';
import { Animal } from './animal';
import { MessageService } from './message.service';

import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { User } from './user';


@Injectable({
  providedIn: 'root'
})
export class UserService {

    private userUrl = 'http://localhost:8080/users/';  // URL to web api
    private baseURL = "http://localhost:8080/";

    httpOptions = {headers: new HttpHeaders({ 'Content-Type': 'application/json' })};

    constructor(private http: HttpClient, private messageService: MessageService) { }
  
    /**
     * Handle Http operation that failed.
     * Let the app continue.
     *
     * @param operation - name of the operation that failed
     * @param result - optional value to return as the observable result
     */
    private handleError<T>(operation = 'operation', result?: T) {
      return (error: any): Observable<T> => {
  
        // TODO: send the error to remote logging infrastructure
        console.error(error); // log to console instead
  
        // TODO: better job of transforming error for user consumption
        this.messageService.add(`${operation} failed: ${error.message}`);
  
        // Let the app keep running by returning an empty result.
        return of(result as T);
      };
    }

    // getUsers
    // gets all the avaible users
    getUsers(): Observable<User[]> {
        const users = this.http.get<User[]>(this.userUrl).pipe(
            tap(_=> this.messageService.add('fetched users')),
            catchError(this.handleError<User[]>('getUsers', []))
        );
        this.messageService.add('UserService: fetched users');
        return users;
    }


    getUser(name: String): Observable<User> {
        const defaultUser: User = {
            name: "name",
            password: "password",
            level: 0,
            basket: []
        };
        const user = this.http.get<User>(this.userUrl + name).pipe(
            tap(_ => this.messageService.add('fetched user')),
            catchError(this.handleError<User>('getUser', defaultUser))
        );
        this.messageService.add('UserService: fetched user');
        return user;
    }

    updateUser(user: User): Observable<any> {
        return this.http.put(this.userUrl, user, this.httpOptions).pipe(
            tap(_ => this.messageService.add('updated user name=${user.name}')),
            catchError(this.handleError<any>('updateUser'))
        )
    }
    
}