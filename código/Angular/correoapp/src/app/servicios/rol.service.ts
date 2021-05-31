import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Rol } from '../modelos/rol';

@Injectable({
  providedIn: 'root'
})
export class RolService {

  private baseEndpoint = 'http://localhost:8090/api/usuarios/roles';
  private cabeceras: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  public listar(): Observable<Rol[]> {
    return this.http.get<Rol[]>(this.baseEndpoint + '/ver');
  }

  public ver(id: number): Observable<Rol> {
    return this.http.get<Rol>(`${this.baseEndpoint}/ver/${id}`);
  }

}
