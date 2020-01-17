import { Component, OnInit } from '@angular/core';
import { TaskService } from 'src/app/service/task/task.service';
import { Router } from '@angular/router';
import { Task } from 'src/app/model/task';
import { StorageService } from 'src/app/service/auth-storage/storage.service';

@Component({
  selector: 'app-view-tasks',
  templateUrl: './view-tasks.component.html',
  styleUrls: ['./view-tasks.component.css']
})
export class ViewTasksComponent implements OnInit {

  private tasks: Task[] = null;
  private role: string;

  constructor(private taskService: TaskService, private router: Router, private tokenService: StorageService) { }

  ngOnInit() {
    this.role = this.tokenService.getRole();
    let x = this.taskService.getAllUserTask(2);

    x.subscribe(res => {
      this.tasks = res;
    }, error =>  {

    });

  }

  view(task: Task){
    let link: string = '/' + task.url + '/' +  task.parameter;
    this.router.navigate([link]);
  }

  removeTask(task: Task){
    let x = this.taskService.removeTask(task.taskId);

    x.subscribe(res => {
      let i: number = this.tasks.indexOf(task);
      this.tasks.splice(i, 1);
    }, erro => {
      
    });
  }

}
