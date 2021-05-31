import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Perfil } from '../modelos/perfil';
import { first, map, tap } from 'rxjs/operators';
import { Empresa } from '../modelos/empresa';
import { URL_BACKEND } from '../config/config';

@Injectable({
  providedIn: 'root'
})
export class PerfilService {

  private baseEndpoint:string =URL_BACKEND+'/perfiles'
  private cabeceras: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  public listar(): Observable<Perfil[]> {
    return this.http.get<Perfil[]>(this.baseEndpoint + '/');
  }

  public ver(id: number): Observable<Perfil> {
    return this.http.get<Perfil>(`${this.baseEndpoint}/${id}`);
  }

  public guardar(perfil: Perfil): Observable<Perfil> {
    return this.http.post<Perfil>(this.baseEndpoint+'/', perfil, { headers: this.cabeceras });
  }

  public eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseEndpoint}/${id}`);
  }

  public guardarConFoto(perfil: Perfil,archivo: File): Observable<Perfil> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    formData.append('nombre', perfil.nombre);
    formData.append('apellidos', perfil.apellidos);
    formData.append('empresa', perfil.empresa.id.toString());
    if(perfil.id){
      formData.append('id', perfil.id.toString());
    }
    formData.append('username', perfil.username);
    formData.append('password', perfil.password);
    formData.append('enabled', String(perfil.enabled));
    formData.append('rol', perfil.rol.toString());
    if (!perfil?.intentos){
      formData.append('intentos', "0");
    }else{
      formData.append('intentos', perfil.intentos?.toString());
    }
    return this.http.post<Perfil>(this.baseEndpoint+'/foto', formData);
  }

  public listarPaginas(page: string, size: string): Observable<any>{
    const params = new HttpParams()
    .set('page', page)
    .set('size', size);
    return this.http.get<any>(`${this.baseEndpoint}/pagina`, {params: params});
  }

}
