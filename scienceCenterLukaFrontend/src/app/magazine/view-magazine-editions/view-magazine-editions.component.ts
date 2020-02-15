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
  selector: 'app-view-magazine-editions',
  templateUrl: './view-magazine-editions.component.html',
  styleUrls: ['./view-magazine-editions.component.css']
})
export class ViewMagazineEditionsComponent implements OnInit {

  private editionId;
  private isLogged: boolean;

  private edition;
  private articles: any[] = [];

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private magazineService: MagazineService, private tokenStorageService: StorageService, 
    private articleService: ArticleService, private cartService: CartService, private toast: ToastrService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const editionId = data.get("editionId");
      this.editionId = editionId;
      
      let x = this.magazineService.getEdition(this.editionId);
      this.isLogged = this.tokenStorageService.isLogged();

      x.subscribe(
        res => {
          console.log(res);
          this.edition = res;

        
        },
        err => {
          console.log("Error occured");
        }
      );

      let y = this.articleService.viewArticles(this.editionId);

      y.subscribe(data => {
        this.articles = data;
      }, err => {
        console.log("Error occured");
      });

    });
  }

  displayArticle(id: string){
    this.router.navigate(['view-article', id])
  }

  buyEdition(){
      const l = this.tokenStorageService.hasCart();
      if(!l){
        this.cartService.createCart().subscribe(newCart => {
          this.tokenStorageService.setCart(newCart);
          const cart: Cart = this.tokenStorageService.getCart();
          let request = new NewCartItemRequest(cart.cartId, this.editionId);
          request.buyingType = BuyingType.MAGAZINE_EDITION;
          this.cartService.addToCart(request).subscribe(data => {
            this.toast.success('Added to cart!')
          });
        });
      } else{
        const cart: Cart = this.tokenStorageService.getCart();
        let request = new NewCartItemRequest(cart.cartId, this.editionId);
        request.buyingType = BuyingType.MAGAZINE_EDITION;
        this.cartService.addToCart(request).subscribe(data => {
          this.toast.success('Added to cart!')
  
        });
      }

  }

}
