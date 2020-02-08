import { Injectable } from '@angular/core';

import { CanActivate } from '@angular/router';
import { StorageService } from '../service/auth-storage/storage.service';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class AdminGuard implements CanActivate {

  constructor(private tokeStorage: StorageService, private toastrService: ToastrService) {}

  // canActivate() {
  //   if(this.tokeStorage.isLogged() && this.tokeStorage.getRole() === 'ADMIN'){
  //     return true;
  //   }else{
  //     this.toastrService.warning('You cannot access to requested path');
  //     return false;
  //   }
  // }

  canActivate() {
    if(this.tokeStorage.isLogged() && this.tokeStorage.isAdmin()){
      return true;
    }else{
      this.toastrService.warning('You cannot access to requested path');
      return false;
    }
  }
}