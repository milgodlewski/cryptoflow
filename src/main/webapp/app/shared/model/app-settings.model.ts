export interface IAppSettings {
  id?: number;
}

export class AppSettings implements IAppSettings {
  constructor(public id?: number) {}
}
