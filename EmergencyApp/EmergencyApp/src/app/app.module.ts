import { NgModule } from '@angular/core';
import { IonicApp, IonicModule } from 'ionic-angular';
import { MyApp } from './app.component';
import { HomePage } from '../pages/home/home';
import { EmergencyServicePage } from '../pages/emergency-service/emergency-service'
import { SymptomsPagePage } from '../pages/symptoms-page/symptoms-page'
import { WaitingRoomPage } from '../pages/waiting-room/waiting-room'

@NgModule({
  declarations: [
    MyApp,
    HomePage,
    EmergencyServicePage,
    SymptomsPagePage,
    WaitingRoomPage
  ],
  imports: [
    IonicModule.forRoot(MyApp)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    HomePage,
    EmergencyServicePage,
    SymptomsPagePage,
    WaitingRoomPage
  ],
  providers: []
})
export class AppModule {}
