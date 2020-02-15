import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MagazineEdition } from 'src/app/model/magazine-edition.dto';
import { MagazineService } from 'src/app/service/magazine/magazine.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-new-edition',
  templateUrl: './new-edition.component.html',
  styleUrls: ['./new-edition.component.css']
})
export class NewEditionComponent implements OnInit {

  private magazineId: string;

  private formData: MagazineEdition = new MagazineEdition();

  constructor(private activatedRoute: ActivatedRoute, private router: Router, private magazineService: MagazineService, private toastrService: ToastrService) { }

  ngOnInit() {

    this.activatedRoute.paramMap.subscribe(data => {
      const magazineId = data.get("magazineId");
      this.magazineId = magazineId;
      
    });
  }

  onSubmit(value, form){
    this.magazineService.addEdition(this.magazineId, this.formData).subscribe(data => {
      this.toastrService.success('New edition added!');
      this.router.navigate(['view-magazine', this.magazineId]);
    }, err => {

    });
  }

}
