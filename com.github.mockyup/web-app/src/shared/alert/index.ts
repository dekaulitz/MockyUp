export enum AlertType {
  WARNING = 'warning',
  ERROR = 'error',
  SUCCESS = 'success',
  INFO = 'info'
}

export interface AlertInterface{
  show:boolean
  icon?:string
  alertType?:AlertType
  closeable?:boolean
  content?:string
}
