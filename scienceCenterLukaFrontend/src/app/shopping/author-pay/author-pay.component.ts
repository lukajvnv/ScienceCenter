import { Component, OnInit } from '@angular/core';
import { ArticleService } from 'src/app/service/article/article.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-author-pay',
  templateUrl: './author-pay.component.html',
  styleUrls: ['./author-pay.component.css']
})
export class AuthorPayComponent implements OnInit {

  constructor(private articleService: ArticleService, private router: Router, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");

      this.articleService.executeAuthorPayment(taskId).subscribe(data => {
        
          this.router.navigate(['/externalRedirect', { externalUrl: data.kpUrl }], {
            skipLocationChange: true,
          });
      
      });

      
    });
  }

}
