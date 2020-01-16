import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MagazineService {

  private MAGAZINEAPI = "http://localhost:8085/magazine/";


  constructor(private http: HttpClient) { }

  startNewMagazine(): Observable<any>{
    return this.http.get(this.MAGAZINEAPI + 'start'); 
  }

  correctNewMagazine(taskId: string){
    return this.http.get(this.MAGAZINEAPI + 'correct/'.concat(taskId));
  }

  retrieveEditorsReviewers(taskId) : Observable<any>{
    return this.http.get(this.MAGAZINEAPI + "/retrieveEditorsReviewers/".concat(taskId));
  }

  postMagazineBasicData(obj, taskId) : Observable<any>{
    return this.http.post(this.MAGAZINEAPI + "/submitBasicInfo/".concat(taskId), obj);
  }

  correctMagazineBasicData(obj, taskId) : Observable<any>{
    return this.http.put(this.MAGAZINEAPI + "/correctBasicInfo/".concat(taskId), obj);
  }

  postMagazineEditorsReviewersData(obj, taskId) : Observable<any>{
    return this.http.post(this.MAGAZINEAPI + "/submitComplexInfo/".concat(taskId), obj);
  }

  checkingMagazineDataStart(taskId) : Observable<any>{
    return this.http.get(this.MAGAZINEAPI + "/checkingMagazineData/".concat(taskId));
  }

  checkingMagazineDataPost(response, taskId) : Observable<any>{
    return this.http.post(this.MAGAZINEAPI + "/checkingMagazineData/".concat(taskId), response);
  }

  getAllMagazines(): Observable<any>{
    return this.http.get(this.MAGAZINEAPI + 'all'); 
  }

  getMagazine(magazineId: string): Observable<any>{
    return this.http.get(this.MAGAZINEAPI + '/' + magazineId); 
  }
}
