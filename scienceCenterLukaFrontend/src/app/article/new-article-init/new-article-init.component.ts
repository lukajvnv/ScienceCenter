import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from 'src/app/service/article/article.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';
import { GenericResponse } from 'src/app/model/GenericResponse';

@Component({
  selector: 'app-new-article-init',
  templateUrl: './new-article-init.component.html',
  styleUrls: ['./new-article-init.component.css']
})
export class NewArticleInitComponent implements OnInit {

  constructor(private activatedRoute: ActivatedRoute, private articleService: ArticleService, private toastrService: ToastrService, private router: Router) {}

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const magazineId = data.get("magazineId");
      
      let x = this.articleService.startNewArticleInitialiaztion(magazineId);

      x.subscribe(
        res  => {
          this.toastrService.info(res.message);
          this.router.navigate(['home']);

        }, err => {
            console.log("Error occured");
            this.toastrService.error(err.error.message);
            this.router.navigate(['home']);
        })
      });
  }
}
