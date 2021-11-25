import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ErrorResponse, ServiceResponse } from '../models/service-response.model';
import { Cart, CartItem, Product, UpdateCartItemRequest } from '../models/product.model';

@Injectable({
    providedIn: 'root'
})
export class CartService {

    private messageSource = new BehaviorSubject(new Cart());
    currentMessage = this.messageSource.asObservable();

    constructor(private http: HttpClient) { }

    getCart(): Observable<ServiceResponse<Cart>> {
        let sessionId = localStorage.getItem('sessionId');
        if(sessionId == undefined) {
            const observable = new Observable<ServiceResponse<Cart>>(subscriber => {
                let resp : ServiceResponse<Cart> = {
                    httpStatus: 404,
                    serviceCode: -1,
                    serviceMessage: "You session has ended.",
                    result: null
                };
                subscriber.next(resp);
            });
            return observable;
        }

        const headers = new HttpHeaders().set('user-session', sessionId);
        return this.http.get<ServiceResponse<Cart>>(
            `${environment.apiURL}/cart`, {headers: headers}
        );
    }

    async getCartAsync(): Promise<Cart | ErrorResponse> {
        let sessionId = localStorage.getItem('sessionId');
        const headers = new HttpHeaders().set('user-session', sessionId);
        const response = await this.http.get<ServiceResponse<Cart>>(
            `${environment.apiURL}/cart`, {headers: headers}
        ).toPromise();
        if(response.serviceCode != 0) {
            const error: ErrorResponse = {
                serviceCode: response.serviceCode,
                serviceMessage: response.serviceMessage
            }
            return error;
        }
        this.messageSource.next(response.result);
        return response.result;
    }

    addToCart(product: Product): Observable<ServiceResponse<Cart>> {
        let sessionId = localStorage.getItem('sessionId');
        if(sessionId == undefined) {
            const observable = new Observable<ServiceResponse<Cart>>(subscriber => {
                let resp : ServiceResponse<Cart> = {
                    httpStatus: 404,
                    serviceCode: -1,
                    serviceMessage: "You session has ended.",
                    result: null
                };
                subscriber.next(resp);
            });
            return observable;
        }
        const headers = new HttpHeaders().set('user-session', sessionId);
        return this.http.post<ServiceResponse<Cart>>(
            `${environment.apiURL}/cart/add`,
            product,
            {headers: headers}
        )
    }

    updateCart(request: UpdateCartItemRequest): Observable<ServiceResponse<CartItem>> {
        let sessionId = localStorage.getItem('sessionId');
        if(sessionId == undefined) {
            const observable = new Observable<ServiceResponse<CartItem>>(subscriber => {
                let resp : ServiceResponse<CartItem> = {
                    httpStatus: 404,
                    serviceCode: -1,
                    serviceMessage: "You session has ended.",
                    result: null
                };
                subscriber.next(resp);
            });
            return observable;
        }
        request.userSessionId = sessionId;
        const headers = new HttpHeaders().set('user-session', sessionId);
        return this.http.put<ServiceResponse<CartItem>>(
            `${environment.apiURL}/api/v1/cart/add`,
            request,
            {headers: headers}
        )
    }
}