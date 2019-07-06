import { Component } from '@angular/core';

import { AuthenticationService } from './service';
import { User } from './model';
import { GlobalService } from './shared/services/global.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css', './app.component.scss' ]
})
export class AppComponent {
  currentUser: User;

  isSidebarToggle: boolean = true;

  public sidebarToggle() {
    /* this._globalService.sidebarToggle$.subscribe(sidebarToggle => {
      this.sidebarToggle = sidebarToggle;
    }, error => {
      console.log('Error: ' + error);
    }); */

    this._globalService.data$.subscribe(data => {
      if (data.ev === 'sidebarToggle') {
        this.isSidebarToggle = data.value;
      }
    }, error => {
      console.log('Error: ' + error);
    });
    this._globalService.dataBusChanged('sidebarToggle', !this.isSidebarToggle);


    //this._globalService._sidebarToggleState(!this.isSidebarToggle);
  }

  constructor(
      private authenticationService: AuthenticationService,
      private _globalService: GlobalService,
      private router: Router
  ) {
      this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
  }
  
}
