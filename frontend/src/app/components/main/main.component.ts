import {Component, OnInit} from '@angular/core';
import {UsersGatewayService} from "../../services/users-gateway.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  username?: string;

  constructor(private userGateway: UsersGatewayService) {
  }

  ngOnInit(): void {
    this.userGateway.getCurrentUser().subscribe(keycloakUser => {
      this.username = keycloakUser.username;
    });
  }

}
