import { Component, OnInit, RootRenderer } from '@angular/core';
import { SignInDto } from 'src/app/model/form-sign-in-submission';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserService } from 'src/app/service/user/user.service';
import { StorageService } from 'src/app/service/auth-storage/storage.service';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {

  private signInDto: SignInDto = new SignInDto();

  constructor(private router: Router, private toastrService: ToastrService, private userService: UserService,
    private tokenStorage: StorageService) { }

  ngOnInit() {

  }

  onSubmit(value, form){
    console.log(this.signInDto);

    let x = this.userService.signIn(this.signInDto);

    x.subscribe(res => {
      console.log(res);
          this.tokenStorage.saveToken(res.accessToken);
          this.tokenStorage.saveUsername(res.username);
          this.tokenStorage.saveAuthorities(res.authorities);
          this.tokenStorage.saveUser(res.user_id);

          this.toastrService.success('Welcome:' + this.signInDto.username + '!');
          this.router.navigate(['home']);
    }, err => {
      this.toastrService.error('Code:'+ err.error.status + ', message:' + err.error.message);
    });
  }

}
