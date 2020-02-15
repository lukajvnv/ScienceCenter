import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentTypeRequest } from 'src/app/model/shopping/payment-type-request.model';
import { PayRequest } from 'src/app/model/shopping/pay-request.model';
import { retry, catchError } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class PayService {

  private SCIENCE_CENTER_API = 'https://localhost:8085/pay/';


  constructor(private http: HttpClient) { }

  getPaymentTypes(request: PaymentTypeRequest): Observable<any> {
    return this.http.post(this.SCIENCE_CENTER_API + 'paymentTypes', request)
    .pipe(retry(1), catchError(this.handlerError));
  }

  buyMagazine(request: PayRequest) : Observable<any> {
    return this.http.post(this.SCIENCE_CENTER_API + 'buy', request)
    .pipe(retry(1), catchError(this.handlerError));
  }

  executePayment(request: any): Observable<any> {
    // workaround
    return this.http.post(this.SCIENCE_CENTER_API + 'cart', request)
    .pipe(retry(1), catchError(this.handlerError));
  }

  private handlerError(error: Response) {
    return Observable.throw(error);
  }
}
