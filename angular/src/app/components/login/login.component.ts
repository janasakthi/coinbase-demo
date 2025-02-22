import { Component } from '@angular/core';
import { NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [NgIf, FormsModule] 
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    this.authService.login(this.username, this.password).subscribe({
      next: () => console.log('Login successful'),
      error: (error) => {
		  console.error('Login failed', error);
		  this.errorMessage = error.message ;
	  }
    });
  }
}
