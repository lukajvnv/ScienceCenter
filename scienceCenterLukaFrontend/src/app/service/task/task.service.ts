import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from '../auth-storage/storage.service';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private TASKAPI = "http://localhost:8085/task/";


  constructor(private http: HttpClient, private tokenStorageService: StorageService) { }

  private genHeader(): HttpHeaders {
    return new HttpHeaders().set('Authorization', this.tokenStorageService.getToken());
  }

  getAllUserTask(id: number): Observable<any> {
    return this.http.get(this.TASKAPI + 'assignedToUser/'.concat("8"), {headers: this.genHeader()});
  }

  getTask(id: number): Observable<any> {
    return this.http.get(this.TASKAPI + "8");
  }

  removeTask(taskId: string): Observable<any> {
    return this.http.get(this.TASKAPI + 'removeTask/'.concat(taskId));
  }
}
