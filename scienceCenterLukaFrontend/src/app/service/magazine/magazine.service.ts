import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from '../auth-storage/storage.service';

@Injectable({
  providedIn: 'root'
})
export class MagazineService {

  private MAGAZINEAPI = "http://localhost:8085/magazine/";


  constructor(private http: HttpClient, private tokenStorageService: StorageService) { }

  private genHeader(): HttpHeaders {
    return new HttpHeaders().set('Authorization', this.tokenStorageService.getToken());
  }

  startNewMagazine(): Observable<any>{
    const header: HttpHeaders = this.genHeader();
    
    return this.http.get(this.MAGAZINEAPI + 'start', {headers: this.genHeader()}); 
  }

  correctNewMagazine(taskId: string){
    return this.http.get(this.MAGAZINEAPI + 'correct/'.concat(taskId));
  }

  retrieveEditorsReviewers(taskId) : Observable<any>{
    return this.http.get(this.MAGAZINEAPI + "retrieveEditorsReviewers/".concat(taskId));
  }

  postMagazineBasicData(obj, taskId) : Observable<any>{
    return this.http.post(this.MAGAZINEAPI + "submitBasicInfo/".concat(taskId), obj);
  }

  correctMagazineBasicData(obj, taskId) : Observable<any>{
    return this.http.put(this.MAGAZINEAPI + "correctBasicInfo/".concat(taskId), obj);
  }

  postMagazineEditorsReviewersData(obj, taskId) : Observable<any>{
    return this.http.post(this.MAGAZINEAPI + "submitComplexInfo/".concat(taskId), obj);
  }

  checkingMagazineDataStart(taskId) : Observable<any>{
    return this.http.get(this.MAGAZINEAPI + "checkingMagazineData/".concat(taskId), {headers: this.genHeader()});
  }

  checkingMagazineDataPost(response, taskId) : Observable<any>{
    return this.http.post(this.MAGAZINEAPI + "checkingMagazineData/".concat(taskId), response, {headers: this.genHeader()});
  }

  // getAllMagazines(): Observable<any>{
  //   return this.http.get('http://localhost:8085/article/magazines', {headers: this.genHeader()}); 
  // }

  getAllMagazines(): Observable<any>{
    return this.http.get('http://localhost:8085/article/magazines'); 
  }

  getMagazine(magazineId: string): Observable<any>{
    return this.http.get('http://localhost:8085/article/magazine/'  + magazineId); 
  }
}
