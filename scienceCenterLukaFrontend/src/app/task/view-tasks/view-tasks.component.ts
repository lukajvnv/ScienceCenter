import { Component, OnInit } from '@angular/core';
import { TaskService } from 'src/app/service/task/task.service';
import { Router } from '@angular/router';
import { Task } from 'src/app/model/task';

@Component({
  selector: 'app-view-tasks',
  templateUrl: './view-tasks.component.html',
  styleUrls: ['./view-tasks.component.css']
})
export class ViewTasksComponent implements OnInit {

  private tasks = null;

  constructor(private taskService: TaskService, private router: Router) { }

  ngOnInit() {

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

    }, erro => {
      
    });
  }

}
