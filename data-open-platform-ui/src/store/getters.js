const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  roles: state => state.user.roles,
  permission_routes: state => state.permission.routes,
  name: state => state.user.name
}
export default getters
