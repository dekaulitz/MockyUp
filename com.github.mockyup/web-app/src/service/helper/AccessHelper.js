// export enum AccessMenu {
//   USERS_READ = 'USERS_READ',
//   USERS_READ_WRITE = 'USERS_READ_WRITE',
//   PROJECTS_READ = 'PROJECTS_READ',
//   PROJECTS_READ_WRITE = 'PROJECTS_READ_WRITE',
//   PROJECT_CONTRACTS_READ_WRITE = 'PROJECT_CONTRACTS_READ_WRITE',
//   PROJECT_CONTRACTS_READ = 'PROJECT_CONTRACTS_READ',
// }
export const hasAccessMenu = (access, accessMenu) => {
    return access.indexOf(accessMenu) > -1;
};
export const AccessData = new Map();
AccessData.set('USERS_READ', {
    label: 'Read Users',
    description: 'Can read users'
});
AccessData.set('USERS_READ_WRITE', {
    label: 'Read and Write Users',
    description: 'Can read and modified users'
});
AccessData.set('PROJECTS_READ', {
    label: 'Read Projects',
    description: 'Can read projects'
});
AccessData.set('PROJECTS_READ_WRITE', {
    label: 'Read and Write Projects',
    description: 'Can read and modified projects'
});
AccessData.set('PROJECT_CONTRACTS_READ_WRITE', {
    label: 'Read and Write Project Contracts',
    description: 'Can read or modified project contracts'
});
AccessData.set('PROJECT_CONTRACTS_READ', {
    label: 'Read Project Contracts',
    description: 'Can view project contracts'
});
//# sourceMappingURL=AccessHelper.js.map