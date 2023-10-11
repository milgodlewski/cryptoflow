<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="ncryptoflowApp.appSettings.home.createOrEditLabel" data-cy="AppSettingsCreateUpdateHeading">
          Create or edit a App Settings
        </h2>
        <div>
          <div class="form-group" v-if="appSettings.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="appSettings.id" readonly />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./app-settings-update.component.ts"></script>
