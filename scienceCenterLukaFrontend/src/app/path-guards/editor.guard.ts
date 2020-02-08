import { Injectable } from '@angular/core';

import { CanActivate } from '@angular/router';
import { StorageService } from '../service/auth-storage/storage.service';

@Injectable()
export class EditorGuard implements CanActivate {

  constructor(private tokenService: StorageService) {}

  canActivate() {
    if(this.tokenService.isEditor()){
      return true;
    }else{
      return false;
    }
  }
}