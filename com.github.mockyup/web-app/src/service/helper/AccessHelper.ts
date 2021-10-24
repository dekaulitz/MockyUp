export interface AccessInterface {
  label: string
  description: string
}

export type accessType =
  'USERS_READ'
  | 'USERS_READ_WRITE'
  | 'PROJECTS_READ'
  | 'PROJECTS_READ_WRITE'
  | 'PROJECT_CONTRACTS_READ_WRITE'
  | 'PROJECT_CONTRACTS_READ'

// export enum AccessMenu {
//   USERS_READ = 'USERS_READ',
//   USERS_READ_WRITE = 'USERS_READ_WRITE',
//   PROJECTS_READ = 'PROJECTS_READ',
//   PROJECTS_READ_WRITE = 'PROJECTS_READ_WRITE',
//   PROJECT_CONTRACTS_READ_WRITE = 'PROJECT_CONTRACTS_READ_WRITE',
//   PROJECT_CONTRACTS_READ = 'PROJECT_CONTRACTS_READ',
// }

export const hasAccessMenu = (access: string[], ...accessMenus: accessType[]): boolean => {
  let hasAccess = false
  // return userAccess.access.indexOf(accessMenu) > -1
  for (const accessMenu of accessMenus) {
    if (access.indexOf(accessMenu) > -1) {
      hasAccess = true
    }
  }
  return hasAccess
}

export const AccessData: Map<string, AccessInterface> = new Map<string, AccessInterface>()
AccessData.set('USERS_READ', {
  label: 'Read Users',
  description: 'Can read users'
})
AccessData.set('USERS_READ_WRITE', {
  label: 'Read and Write Users',
  description: 'Can read and modified users'
})
AccessData.set('PROJECTS_READ', {
  label: 'Read Projects',
  description: 'Can read projects'
})
AccessData.set('PROJECTS_READ_WRITE', {
  label: 'Read and Write Projects',
  description: 'Can read and modified projects'
})
AccessData.set('PROJECT_CONTRACTS_READ_WRITE', {
  label: 'Read and Write Project Contracts',
  description: 'Can read or modified project contracts'
})
AccessData.set('PROJECT_CONTRACTS_READ', {
  label: 'Read Project Contracts',
  description: 'Can view project contracts'
})
