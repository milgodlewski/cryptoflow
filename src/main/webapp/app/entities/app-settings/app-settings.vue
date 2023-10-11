<template>
  <div>
    <h2 id="page-heading" data-cy="AppSettingsHeading">
      <span id="app-settings-heading">App Settings</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'AppSettingsCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-app-settings"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new App Settings</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && appSettings && appSettings.length === 0">
      <span>No App Settings found</span>
    </div>
    <div class="table-responsive" v-if="appSettings && appSettings.length > 0">
      <table class="table table-striped" aria-describedby="appSettings">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="appSettings in appSettings" :key="appSettings.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AppSettingsView', params: { appSettingsId: appSettings.id } }">{{ appSettings.id }}</router-link>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AppSettingsView', params: { appSettingsId: appSettings.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AppSettingsEdit', params: { appSettingsId: appSettings.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(appSettings)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="ncryptoflowApp.appSettings.delete.question" data-cy="appSettingsDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-appSettings-heading">Are you sure you want to delete App Settings {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-appSettings"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeAppSettings()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./app-settings.component.ts"></script>
