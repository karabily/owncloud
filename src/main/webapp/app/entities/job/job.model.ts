import { BaseEntity, User } from './../../shared';

export class Job implements BaseEntity {
    constructor(
        public id?: number,
        public nameFileContentType?: string,
        public nameFile?: any,
        public name?: string,
        public userToJob?: User,
    ) {
    }
}
