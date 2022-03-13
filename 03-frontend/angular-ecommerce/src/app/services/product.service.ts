import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from '../common/product';
import { Observable } from 'rxjs';  // rxjs is short for Reactive JavaScript
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl = 'http://localhost:9091/api/products';

  // inject the httpClient
  constructor(private httpClient: HttpClient) { }

  // this maps the JSON data from Spring Data REST to the Product array
  getProductList(): Observable<Product[]> {
    return this.httpClient.get<GetResponse>(this.baseUrl).pipe(
      map(response => response._embedded.products)
    );
  }
}

// this unwraps the JSON from Spring Data Rest into _embedded entry
interface GetResponse {
  _embedded: {
    products: Product[]
  }
}
