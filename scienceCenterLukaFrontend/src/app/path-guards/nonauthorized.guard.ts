import { Injectable } from '@angular/core';

import { CanActivate } from '@angular/router';
import { StorageService } from '../service/auth-storage/storage.service';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class Notauthorized implements CanActivate {

  constructor(private tokeStorage: StorageService, private toastrService: ToastrService) {}

  canActivate() {
    if(!this.tokeStorage.isLogged()){
      return true;
    }else{
      this.toastrService.warning('You cannot access to requested path');
      return false;
    }
  }
}