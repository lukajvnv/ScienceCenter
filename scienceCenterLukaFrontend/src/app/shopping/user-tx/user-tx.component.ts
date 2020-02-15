import { Component, OnInit, Input } from '@angular/core';
import { CartService } from 'src/app/service/shopping/cart.service';
import { UserTx } from 'src/app/model/user/user-tx.model';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-tx',
  templateUrl: './user-tx.component.html',
  styleUrls: ['./user-tx.component.css']
})
export class UserTxComponent implements OnInit {

  private userTxs: UserTx[] = []

  private selectedTx: UserTx;

  @Input('tx-type') type: string;

  constructor(private cartService: CartService, private toast: ToastrService, private router: Router) { }

  ngOnInit() {
    if(this.type == 'success-txs'){
      this.cartService.getSuccessUserTx().subscribe(data => {
        this.userTxs = data;
      });
    } else if (this.type == 'other-txs'){
      this.cartService.getOtherUserTx().subscribe(data => {
        this.userTxs = data;
      });
    } else {
      console.log('error');
    }
    
  }

  click(tx: UserTx){
    this.selectedTx = tx;
  }

  checkTx(tx: UserTx){
    var obj: {[k: string]: any} = {};
    obj.txInfoId = tx.userTxId;

    let x = this.cartService.checkTx(obj);

    x.subscribe(data => {
      this.toast.success('tx status:' + data.status);
      this.router.navigate(['profile']);
    }, err => {
      console.log('err');
      this.toast.error('erro occured, try again!');
    });
  }

}
