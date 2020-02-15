import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MagazineService } from 'src/app/service/magazine/magazine.service';
import { StorageService } from 'src/app/service/auth-storage/storage.service';
import { ArticleService } from 'src/app/service/article/article.service';
import { Cart } from 'src/app/model/shopping/shoppingcart.model';
import { NewCartItemRequest } from 'src/app/model/shopping/shoppingcart-new-item-request.model';
import { BuyingType } from 'src/app/model/shopping/buying.type.enum';
import { CartService } from 'src/app/service/shopping/cart.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-view-article',
  templateUrl: './view-article.component.html',
  styleUrls: ['./view-article.component.css']
})
export class ViewArticleComponent implements OnInit {

  private articleId;
  private isLogged: boolean;

  private displayArticle;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private cartService: CartService, 
    private articleService: ArticleService, private tokenStorageService: StorageService, private toast: ToastrService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const articleId = data.get("articleId");
      this.articleId = articleId;
      
      let x = this.articleService.viewArticle(this.articleId);
      this.isLogged = this.tokenStorageService.isLogged();

      x.subscribe(
        res => {
          console.log(res);
          this.displayArticle = res;

        
        },
        err => {
          console.log("Error occured");
        }
      );
    });
  }

  download(articleId) {
    this.articleService.downloadFileByUser(articleId).subscribe( (data: Blob )=> {
			
      var file = new Blob([data], { type: 'application/pdf' })
      var fileURL = URL.createObjectURL(file);

      window.open(fileURL); 
      var a         = document.createElement('a');
      a.href        = fileURL; 
      a.target      = '_blank';
      a.download    = 'bill.pdf';
      document.body.appendChild(a);
      a.click();
      
		}, error =>  {
      console.log('Error downloading the file');
      this.toast.error('First you need to buy article!');
    });  
  }

  addToCart(article){
    const l = this.tokenStorageService.hasCart();
    if(!l){
      this.cartService.createCart().subscribe(newCart => {
        this.tokenStorageService.setCart(newCart);
        const cart: Cart = this.tokenStorageService.getCart();
        let request = new NewCartItemRequest(cart.cartId, article.articleId);
        request.buyingType = BuyingType.ARTICLE;
        this.cartService.addToCart(request).subscribe(data => {
          this.toast.success('Added to cart!')
        });
      });
    } else{
      const cart: Cart = this.tokenStorageService.getCart();
      let request = new NewCartItemRequest(cart.cartId, article.articleId);
      request.buyingType = BuyingType.ARTICLE;
      this.cartService.addToCart(request).subscribe(data => {
        this.toast.success('Added to cart!')

      });
    }
    
  }

}
