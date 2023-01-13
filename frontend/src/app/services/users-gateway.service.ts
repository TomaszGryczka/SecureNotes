import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UsersGatewayService {

  private apiUri = "http://localhost:8080/users"

  constructor(private httpClient: HttpClient) {
  }

  getAllUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.apiUri + "/all");
  }

  getCurrentUser(): Observable<User> {
    return this.httpClient.get<User>(this.apiUri + "/current")
  }
}

export interface User {
  id: string;
  username: string;
}
