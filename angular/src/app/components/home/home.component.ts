import { Component, OnInit,ViewChild } from '@angular/core';
import { NgIf, CommonModule,DatePipe } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormGroup,FormControl } from '@angular/forms';
import { HistoryService } from '../../services/history.service';
import { AuthService } from '../../services/auth.service';
//import { ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';

import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar'; 

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  imports: [NgIf, FormsModule, CommonModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
	MatPaginatorModule,
	MatProgressBarModule],
	providers: [DatePipe]
})
export class HomeComponent {
 errorMessage = '';
  code = 'BTC';
  historyData: any = null;
  dateRangeForm: FormGroup;
 loading: boolean = false; 
 cacheEnabled: boolean = localStorage.getItem('cacheEnabled') === 'true';
  
   // Add displayedColumns property for the Material table
  displayedColumns: string[] = ['date', 'price'];
  
  dataSource = new MatTableDataSource<any>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;


  constructor(private historyService: HistoryService, 
	private datePipe: DatePipe,
	private authService: AuthService) {
	 const today = new Date();
    const lastWeek = new Date();
    lastWeek.setDate(today.getDate() - 7);

    this.dateRangeForm = new FormGroup({
      fromDate: new FormControl(lastWeek),   // Default to 7 days ago
      toDate: new FormControl(today)         // Default to today
    });
  }

  ngOnInit() {
    this.getHistory();
  }
  
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }
  
  toggleCache() {
    localStorage.setItem('cacheEnabled', String(this.cacheEnabled));
  }
  
  getHistory() {
	this.errorMessage = '';
    this.loading = true; 

    const fromDate = this.datePipe.transform(this.dateRangeForm.value.fromDate, 'dd-MM-yyyy');
    const toDate = this.datePipe.transform(this.dateRangeForm.value.toDate, 'dd-MM-yyyy');
	const cacheKey = `history_${this.code}_${fromDate}_${toDate}`;
	
	 if (this.cacheEnabled) {		
		const cachedData = localStorage.getItem(cacheKey);
		  if (cachedData) {
			this.loadCachedData(cachedData);
			this.loading = false;
			return;
		  }
	}
	
    this.historyService.getHistory(this.code, fromDate!, toDate!).subscribe({
      next: (data) => {
		  if (data && data.priceIndices) {
			this.historyData = data;
			const sortedData = Object.entries(data.priceIndices)
            .map(([date, price]) => ({ date, price }))
            .sort((a, b) => this.parseDate(a.date) - this.parseDate(b.date)); // Sort by date

            this.dataSource.data = sortedData;
			localStorage.setItem(cacheKey, JSON.stringify(data));
			setTimeout(() => {
			  this.dataSource.paginator = this.paginator;
			});
		  } else {
			this.historyData = null;
			this.dataSource.data = [];
		  }
		this.loading = false;		
      },
      error: (error) => {
        console.error('Error fetching history:', error);
		this.errorMessage = error.message;
		if (this.cacheEnabled) {
          const cachedData = localStorage.getItem(cacheKey);
          if (cachedData) {
            this.loadCachedData(cachedData);
            return;
          }
        }
		this.historyData = null;
		this.dataSource.data = [];
		this.loading = false;
	  }
    });
  }
  
   loadCachedData(cachedData: string) {
    const parsedData = JSON.parse(cachedData);
    this.historyData = parsedData;
    this.dataSource.data = Object.entries(parsedData.priceIndices)
      .map(([date, price]) => ({ date, price }))
      .sort((a, b) => this.parseDate(a.date) - this.parseDate(b.date));

    this.dataSource.paginator = this.paginator;
  }
  
   // Helper function to parse date in DD-MM-YYYY format
  parseDate(dateStr: string): number {
    const parts = dateStr.split('-');
    return new Date(Number(parts[2]), Number(parts[1]) - 1, Number(parts[0])).getTime();
  }
  
  logout() {
	  this.authService.logout();
	}
}
