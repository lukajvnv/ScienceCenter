import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NewCartItemRequest } from 'src/app/model/shopping/shoppingcart-new-item-request.model';
import { Observable } from 'rxjs';
import { StorageService } from '../auth-storage/storage.service';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private SCIENCE_CENTER_API = 'https://localhost:8085/cart/';
  private SCIENCE_CENTER_API_PAY_CONTROLLER = 'https://localhost:8085/pay/';


  constructor(private http: HttpClient, private tokenStorageService: StorageService) { }

  private genHeader(): HttpHeaders {
    return new HttpHeaders().set('Authorization', this.tokenStorageService.getToken());
  }

  addToCart(request: NewCartItemRequest) : Observable<any> {
    return this.http.post( this.SCIENCE_CENTER_API + 'addItemToCart', request, {headers: this.genHeader()});
  }

  createCart() : Observable<any> {
    return this.http.get(this.SCIENCE_CENTER_API +  'newCart', {headers: this.genHeader()});
  }

  getCart(id: number) : Observable<any> {
    return this.http.get(this.SCIENCE_CENTER_API + 'getCart/' + id, {headers: this.genHeader()});
  }

  removeCart(id: string) : Observable<any> {
    return this.http.get(this.SCIENCE_CENTER_API +  'removeItemFromCart/' + id, {headers: this.genHeader()});
  }

  getSuccessUserTx() : Observable<any> {
    return this.http.get(this.SCIENCE_CENTER_API + 'getUserTxs', {headers: this.genHeader()});
  }

  getOtherUserTx() : Observable<any> {
    return this.http.get(this.SCIENCE_CENTER_API + 'getUserTxsOther', {headers: this.genHeader()});
  }

  checkTx(body) : Observable<any> {
    return this.http.post(this.SCIENCE_CENTER_API_PAY_CONTROLLER + 'checkTx', body, {headers: this.genHeader()});
  }
}
