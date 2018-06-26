import { BaseEntity, User } from './../../shared';

export class UserSup implements BaseEntity {
    constructor(
        public id?: number,
        public userSup?: User,
    ) {
    }
}
