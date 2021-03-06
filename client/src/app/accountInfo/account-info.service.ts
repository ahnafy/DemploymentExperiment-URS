import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../user";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable()
export class AccountInfoService {
    readonly baseUrl: string = environment.API_URL + 'users/';
    private editUserUrl: string = this.baseUrl;

    constructor(private http: HttpClient) {
    }

    saveShirtSize(newUser: User): Observable<{ShirtSize: string}> {
        this.editUserUrl = this.baseUrl;
        const httpOptions = {
            headers: new HttpHeaders({
                'Content-Type': 'application/json'
            }),
        };

        return this.http.put<{ShirtSize: string}>(this.editUserUrl + newUser.SubjectID, newUser, httpOptions);
    }

}
