import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { UserSup } from './user-sup.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<UserSup>;

@Injectable()
export class UserSupService {

    private resourceUrl =  SERVER_API_URL + 'api/user-sups';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/user-sups';

    constructor(private http: HttpClient) { }

    create(userSup: UserSup): Observable<EntityResponseType> {
        const copy = this.convert(userSup);
        return this.http.post<UserSup>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(userSup: UserSup): Observable<EntityResponseType> {
        const copy = this.convert(userSup);
        return this.http.put<UserSup>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<UserSup>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<UserSup[]>> {
        const options = createRequestOption(req);
        return this.http.get<UserSup[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<UserSup[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<UserSup[]>> {
        const options = createRequestOption(req);
        return this.http.get<UserSup[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<UserSup[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: UserSup = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<UserSup[]>): HttpResponse<UserSup[]> {
        const jsonResponse: UserSup[] = res.body;
        const body: UserSup[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to UserSup.
     */
    private convertItemFromServer(userSup: UserSup): UserSup {
        const copy: UserSup = Object.assign({}, userSup);
        return copy;
    }

    /**
     * Convert a UserSup to a JSON which can be sent to the server.
     */
    private convert(userSup: UserSup): UserSup {
        const copy: UserSup = Object.assign({}, userSup);
        return copy;
    }
}
