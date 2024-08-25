import { Injectable } from '@angular/core';
import { Animal } from './animal';
import { MessageService } from './message.service';

import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class AnimalService {

  private animalsUrl = 'http://localhost:8080/animals';  // URL to web api
  private animalUrl = 'http://localhost:8080/animals/';  // URL to web api
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

  getAnimals(): Observable<Animal[]> {
    const animals = this.http.get<Animal[]>(this.animalsUrl).pipe(
      tap(_ => this.messageService.add('fetched animals')),
      catchError(this.handleError<Animal[]>('getAnimals', []))
    );
    this.messageService.add('AnimalService: fetched animals');
    return animals;
  }

  getAnimal(id: number): Observable<Animal> {
    const defaultAnimal: Animal = {
      id: 0,
      name: "name",
      type: "type",
      breed: "breed",
      sex: "sex",
      age: 0,
      cost: 0,
      sold: false
    };
    const animal = this.http.get<Animal>(this.animalUrl + id).pipe(
      tap(_ => this.messageService.add('fetched animals')),
      catchError(this.handleError<Animal>('getAnimals', defaultAnimal))
    );
    this.messageService.add('AnimalService: fetched animals');
    return animal;
  }

  searchAnimal(str: string): Observable<Animal[]> {
    const animals = this.http.get<Animal[]>(this.baseURL + "animals/?name=" + str).pipe(
      tap(_ => this.messageService.add('fetched animals')),
      catchError(this.handleError<Animal[]>('searchAnimal', []))
    );
    this.messageService.add('AnimalService: fetched animals');
    return animals;
  }

  searchType(str: string): Observable<Animal[]> {
    const animals = this.http.get<Animal[]>(this.baseURL + "animals/type/?type=" + str).pipe(
      tap(_ => this.messageService.add('fetched animals')),
      catchError(this.handleError<Animal[]>('searchType', []))
    );
    return animals;
  }
  
  updateAnimal(animal: Animal): Observable<any> {
    return this.http.put(this.animalsUrl, animal, this.httpOptions).pipe(
      tap(_ => this.messageService.add('updated animal id=${animal.id}')),
      catchError(this.handleError<any>('updateAnimal'))
    );
  }

  createAnimal(animal: Animal): Observable<any> {
    return this.http.post<Animal>(this.animalsUrl, animal, this.httpOptions).pipe(
      tap((newAnimal: Animal) => this.messageService.add(`added animal w/ id=${newAnimal.id}`)),
      catchError(this.handleError<Animal>('addAnimal'))
    );
  }

  deleteAnimal(id: number): Observable<Animal> {
    const url = `${this.animalsUrl}/${id}`;
    
    return this.http.delete<Animal>(url, this.httpOptions).pipe(
      tap(_ => this.messageService.add(`deleted animal id=${id}`)),
      catchError(this.handleError<Animal>('deleteAnimal'))
    );
  }

}
