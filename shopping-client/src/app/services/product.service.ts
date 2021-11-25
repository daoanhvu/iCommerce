import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SearchModel } from '../models/search-product.model';
import { ServiceResponse, PagedResult } from '../models/service-response.model';
import { Product } from '../models/product.model';

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    constructor(private http: HttpClient) { }

    searchProduct(data: SearchModel): Observable<ServiceResponse<PagedResult<Product>>> {
        return this.http.post<ServiceResponse<PagedResult<Product>>>(
            `${environment.apiURL}/products/search`,
            data
        );
    }
}