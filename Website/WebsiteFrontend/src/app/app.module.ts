import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { ProfessionalComponent } from './components/professional/professional.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { PersonalComponent } from './components/personal/personal.component';
import { ContactComponent } from './components/contact/contact.component';
import { ContactService } from './service/contact.service';
import { SafePipe } from './core/pipe/safepipe.service';
import { AboutComponent } from './about/about.component';

import { AppRoutingModule } from './app-routing.module';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpModule
  ],
  declarations: [
    AppComponent,
    ProfessionalComponent,
    PersonalComponent,
    ProjectsComponent,
    ContactComponent,
    SafePipe,
    AboutComponent
    ],
  providers: [ ContactService ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
