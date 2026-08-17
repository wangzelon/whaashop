import axios from 'axios'
export type Role='ADMIN'|'USER'
export type OrderStatus='PENDING_PAYMENT'|'PAID'|'SHIPPED'|'COMPLETED'|'CLOSED'
export interface ApiResponse<T>{success:boolean;data:T;message:string}
export interface Product{id:number;categoryId:number;name:string;subtitle?:string;mainImage?:string;detailHtml:string;minPrice:number;sales:number;published:boolean;createdAt?:string}
export interface Sku{id:number;productId:number;specJson:string;price:number;stock:number;enabled:boolean}
export interface ShopOrder{id:number;orderNo:string;userId:number;status:OrderStatus;totalAmount:number;receiverName?:string;receiverPhone?:string;receiverAddress?:string;shippedAt?:string;completedAt?:string;createdAt:string}
export interface Review{id:number;orderItemId:number;productId:number;userId:number;rating:number;content:string;imagesJson:string;appendCount:number;hidden:boolean;createdAt:string}
export interface UserProfile{id:number;username:string;nickname:string;avatarUrl?:string;bio?:string;gender:'MALE'|'FEMALE'|'UNSPECIFIED';birthday?:string;role:Role;createdAt:string}
export const api=axios.create({baseURL:import.meta.env.VITE_API_BASE_URL??'http://localhost:8080/api/v1',timeout:15000})
api.interceptors.request.use(c=>{const token=localStorage.getItem('token');if(token)c.headers.Authorization=`Bearer ${token}`;return c})
api.interceptors.response.use(r=>r,e=>Promise.reject(new Error(e.response?.data?.message??e.message)))
export async function login(username:string,password:string){const {data}=await api.post<ApiResponse<{token:string;role:Role;nickname:string}>>('/auth/login',{username,password});localStorage.setItem('token',data.data.token);localStorage.setItem('role',data.data.role);localStorage.setItem('nickname',data.data.nickname);return data.data}
export const logout=()=>{localStorage.removeItem('token');localStorage.removeItem('role');localStorage.removeItem('nickname')}
