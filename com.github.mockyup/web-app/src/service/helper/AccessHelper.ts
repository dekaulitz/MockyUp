export interface AccessInterface {
  label: string
  description: string
}

export const AccessData: Map<string, AccessInterface> = new Map<string, AccessInterface>()
AccessData.set('MOCKS_READ', {
  label: 'Read Mocks',
  description: 'Can read mocks'
})
AccessData.set('MOCKS_READ_WRITE', {
  label: 'Read and Write Mocks',
  description: 'Can read and modified mocks'
})
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
