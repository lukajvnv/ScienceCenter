import { Component, OnInit } from '@angular/core';
import { ArticleService } from 'src/app/service/article/article.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Magazine } from 'src/app/model/magazine';
import { MagazineService } from 'src/app/service/magazine/magazine.service';

@Component({
  selector: 'app-add-to-kp',
  templateUrl: './add-to-kp.component.html',
  styleUrls: ['./add-to-kp.component.css']
})
export class AddToKpComponent implements OnInit {

  constructor(private magazineService: MagazineService, private router: Router, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");

      this.magazineService.addMagazineToKp(taskId).subscribe(data => {
        
          this.router.navigate(['/externalRedirect', { externalUrl: data.url }], {
            skipLocationChange: true,
          });
      
      });

      
    });
  }

}
