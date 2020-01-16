import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private TASKAPI = "http://localhost:8085/task/";


  constructor(private http: HttpClient) { }

  getAllUserTask(id: number): Observable<any> {
    return this.http.get(this.TASKAPI + 'assignedToUser/'.concat("8"));
  }

  getTask(id: number): Observable<any> {
    return this.http.get(this.TASKAPI + "8");
  }

  removeTask(taskId: string): Observable<any> {
    return this.http.get(this.TASKAPI + 'removeTask/'.concat(taskId));
  }
}
