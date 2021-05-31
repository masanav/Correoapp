import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Usuario } from '../modelos/usuario';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private baseEndpoint:string ='http://localhost:8090/api/usuarios/usuarios'
  private cabeceras: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  public listar(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.baseEndpoint + '/ver');
  }

  public ver(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseEndpoint}/ver/${id}`);
  }

  public buscar(username: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseEndpoint}/buscar/username?=${username}`);
  }

  public guardar(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.baseEndpoint}/guardar`, usuario, { headers: this.cabeceras });
  }

  public eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseEndpoint}/ver/${id}`);
  }

  public guardarConFoto(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(this.baseEndpoint, usuario, { headers: this.cabeceras });
  }
}
