import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from '../common/product';
import { Observable } from 'rxjs';  // rxjs is short for Reactive JavaScript
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl = 'http://localhost:8091/api/products';

  // inject the httpClient
  constructor(private httpClient: HttpClient) { }

  // this maps the JSON data from Spring Data REST to the Product array
  getProductList(theCategoryId: number): Observable<Product[]> {

    // need to build url based on category id
    const searchUrl = `${this.baseUrl}/search/findByCategoryId?id=${theCategoryId}`;

    return this.httpClient.get<GetResponse>(searchUrl).pipe(
      map(response => response._embedded.products)
    );
  }
}

// this unwraps the JSON from Spring Data Rest into _embedded entry
interface GetResponse {
  _embedded: {
    products: Product[];
  }
}
