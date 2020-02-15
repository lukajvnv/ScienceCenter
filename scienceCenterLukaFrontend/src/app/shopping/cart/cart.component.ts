import { Component, OnInit } from '@angular/core';
import { Cart } from 'src/app/model/shopping/shoppingcart.model';
import { Router } from '@angular/router';
import { TestService } from 'src/app/service/test.service';
import { PayService } from 'src/app/service/shopping/pay.service';
import { CartService } from 'src/app/service/shopping/cart.service';
import { StorageService } from 'src/app/service/auth-storage/storage.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  private cartSession: Cart;
  private cartDisplay;

  constructor(private cartService: CartService, private router: Router, private tokenStorageService: StorageService, private payService: PayService, private toast: ToastrService) { }

  ngOnInit() {
    let hasCart = this.tokenStorageService.hasCart();
    if(!hasCart){
      this.toast.error('Cart is empty');
      this.router.navigate(['']);
    }else{
       this.cartSession = this.tokenStorageService.getCart();

      this.cartService.getCart(+this.cartSession.cartId).subscribe(data => {
        this.cartDisplay = data;
      });
    }
  }

  
  removeFromCart(item) {
    this.cartService.removeCart(item.userTxItemId).subscribe(data => {
      this.cartService.getCart(+this.cartSession.cartId).subscribe(data => {
        this.cartDisplay = data;
      });
    });
  }

  checkout() {
      let shoppingCartTemp: Cart = new Cart();
      let cart: Cart = this.tokenStorageService.getCart();
     
      this.payService.executePayment(cart).subscribe(data => {
        this.tokenStorageService.removeCart();
        this.router.navigate(['/externalRedirect', { externalUrl: data.kpUrl }], {
          skipLocationChange: true,
            });
      
      });
  }

}
